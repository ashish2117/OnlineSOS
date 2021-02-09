/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveronlinesos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.ConnectException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author user
 */
public class GameThread extends Thread {

    String player1;
    String player2;
    Socket player1Socket;
    Socket player2Socket;
    Game game;
    BufferedReader reader;
    BufferedReader player1Readerl;
    BufferedReader player2Reader;
    MessageSender player1Sender;
    MessageSender player2Sender;

    public GameThread(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.player1Socket = AcceptNewUser.onlineUsrersSocktes.get(player1);
        this.player2Socket = AcceptNewUser.onlineUsrersSocktes.get(player2);
        try {
            this.player1Readerl = new BufferedReader(new InputStreamReader(player1Socket.getInputStream()));
            this.player2Reader = new BufferedReader(new InputStreamReader(player2Socket.getInputStream()));
            player1Sender = new MessageSender(player1Socket);
            player2Sender = new MessageSender(player2Socket);
            reader = player1Readerl;
        } catch (IOException ex) {
            System.out.println("Error in initialising readers and senders");
        }
        game = new Game(player1, player2);
        
    }

    @Override
    public void run() {
        boolean gameOver = false;
        AuthenticationManager.setIsPlaying(player2, true);
        AuthenticationManager.setIsPlaying(player1, true);
        String message;
        JSONObject jsonMessage;
        ArrayList<Location[]> strikedLocs = new ArrayList<>();
        System.out.println("Game started");
        while (!gameOver) {
            try {
                message = reader.readLine();
                System.out.println(message);
                jsonMessage = new JSONObject(message);
                if (jsonMessage.getString("Type").equals("CELLCLICKED")) {
                    int i = jsonMessage.getInt("iVal");
                    int j = jsonMessage.getInt("jVal");
                    short val = (short) jsonMessage.getInt("Val");
                    strikedLocs = game.changeState(i, j, val);
                    System.out.println("CurrentPlayer=" + game.getCurrentPlayer());
                    if (game.getCurrentPlayer().equals(player1)) {
                        player1Sender.sendMessage(jsonMessage);
                    } else if (game.getCurrentPlayer().equals(player2)) {
                        player2Sender.sendMessage(jsonMessage);
                    }
                    if (strikedLocs.size() > 0) {
                        System.out.println("Striked");
                        JSONObject obj = new JSONObject();
                        obj.put("Type", "STRIKEDLOCS");
                        obj.put("player1", game.getPlayer1Points());
                        obj.put("player2", game.getPlayer2Points());
                        obj.put("iVal", i);
                        obj.put("jVal", j);
                        obj.put("Val", val);
                        JSONArray strikedLocsArray = new JSONArray();
                        JSONObject strike;
                        for (Location[] loc : strikedLocs) {
                            String strikeString = loc[0].getX() + " " + loc[0].getY() + " " + loc[1].getX() + " " + loc[1].getY() + " " + loc[2].getX() + " " + loc[2].getY();
                            strike = new JSONObject();
                            strike.put("STRIKES", strikeString);
                            strikedLocsArray.put(strike);
                        }
                        System.out.println("Striked Array " + strikedLocsArray.toString());
                        obj.put("STRIKED_LOCS_ARRAY", strikedLocsArray.toString());
                        System.out.println("Striked Messgae " + obj.toString());
                        player1Sender.sendMessage(obj);
                        player2Sender.sendMessage(obj);
                    } else {
                        reader = game.getCurrentPlayer().equals(player1) ? player1Readerl : player2Reader;
                    }
                }

                if (game.getCount() == 49) {
                    JSONObject finalResult = new JSONObject();
                    finalResult.put("Type", "GAME_RESULT");
                    String winner;
                    if (game.getPlayer1Points() > game.getPlayer2Points()) {
                        winner = player1;
                    } else if (game.getPlayer1Points() < game.getPlayer2Points()) {
                        winner = player2;
                    } else {
                        winner = "draw";
                    }
                    finalResult.put("Winner", winner);
                    player1Sender.sendMessage(finalResult);
                    player2Sender.sendMessage(finalResult);
                    gameOver = true;
                }
            } catch (SocketException ex) {
                
                if (player1Socket.isConnected()) {
                    JSONObject errorMessage = new JSONObject();
                    try {

                        errorMessage.put("Type", "ERROR");
                        errorMessage.put("Message", player2 + " left the game or is no more online");
                        player1Sender.sendMessage(errorMessage);
                    } catch (JSONException ex1) {
                        Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex1);
                    } catch (IOException ex1) {
                        Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    AuthenticationManager.makeUseroffline(player1);
                }
                if(player2Socket.isConnected()){
                    JSONObject errorMessage = new JSONObject();
                    try {

                        errorMessage.put("Type", "ERROR");
                        errorMessage.put("Message", player1 + " left the game or is no more online");
                        player2Sender.sendMessage(errorMessage);
                    } catch (JSONException ex1) {
                        Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex1);
                    } catch (IOException ex1) {
                        Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                    AuthenticationManager.makeUseroffline(player1);
                }
                AuthenticationManager.setIsPlaying(player1, false);
                AuthenticationManager.setIsPlaying(player2, false);
                return;
            } catch (IOException ex) {
                Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JSONException ex) {
                Logger.getLogger(GameThread.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }
}
