package com.handsome.landlords.client.javafx.entity;

import com.handsome.landlords.entity.Poker;
import com.handsome.landlords.entity.PokerSell4P;
import com.handsome.landlords.enums.ClientType;
import com.handsome.landlords.robot.TrusteeBot;

import java.util.List;
import java.util.stream.Collectors;

public class User {
    private String nickname;

    private boolean playing = false;
    private int currentRoomId = -1;
    private List<Poker> pokers;

    // null, ClientType.LANDLORD, ClientType.PEASANT
    private ClientType role;

    //托管机器人
    private TrusteeBot trusteeBot;

    //是否被托管
    private boolean trusted;

    public User(String nickname) {
        this.nickname = nickname;
        this.trusteeBot = new TrusteeBot();
    }

    public void joinRoom(int roomId) {
        currentRoomId = roomId;
        playing = true;
    }

    public PokerSell4P hint(List<Poker> lastSell){
        return trusteeBot.hint(this.pokers, lastSell);
    }

    public void exitRoom() {
        currentRoomId = -1;
        playing = false;
        role = null;
        if (pokers != null) {
            pokers.clear();
            pokers = null;
        }
    }

    public void clearPokers() {
        pokers.clear();
        hintClear();
    }

    public void hintClear(){
        trusteeBot.clear();
    }

    public void addPokers(List<Poker> pokers) {
        if (this.pokers != null) {
            this.pokers.addAll(pokers);
        } else {
            this.pokers = pokers;
        }

        this.pokers = this.pokers.stream()
                                 .sorted((a, b) -> Integer.compare(b.getLevel().getLevel(), a.getLevel().getLevel()))
                                 .collect(Collectors.toList());
    }

    public void removePokers(List<Poker> sellPokerList) {
        for (Poker sellPoker : sellPokerList) {
            pokers.remove(sellPoker);
        }
    }

    public List<Poker> getPokers() {
        return pokers;
    }

    public ClientType getRole() {
        return role;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setRole(ClientType role) {
        this.role = role;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isTrusted() {
        return trusted;
    }

    public void setTrusted() {
        if(!isTrusted()) {
            trusted = true;
        }
    }

    public void cancelTrusted(){
        if(isTrusted()) {
            trusted = false;
        }
    }
}
