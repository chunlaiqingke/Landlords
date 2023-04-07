package com.handsome.landlords.client.javafx.entity;

import com.handsome.landlords.enums.ClientType;

public class CurrentRoomInfo4P extends CurrentRoomInfo{
    private String crossPlayerName;
    private ClientType crossPlayerRole;
    private int crossPlayerSurplusPokerCount;

    public void setLandlord(String landlord){
        if (landlord.equals(super.getPlayer().getNickname())) {
            super.getPlayer().setRole(ClientType.LANDLORD);
            super.setPrevPlayerRole(ClientType.PEASANT);
            super.setNextPlayerRole(ClientType.PEASANT);
            crossPlayerRole = ClientType.PEASANT;
        } else if (landlord.equals(super.getPrevPlayerName())) {
            super.getPlayer().setRole(ClientType.PEASANT);
            super.setPrevPlayerRole(ClientType.LANDLORD);
            super.setNextPlayerRole(ClientType.PEASANT);
            crossPlayerRole = ClientType.PEASANT;
        } else if(landlord.equals(super.getNextPlayerName())) {
            super.getPlayer().setRole(ClientType.PEASANT);
            super.setPrevPlayerRole(ClientType.PEASANT);
            super.setNextPlayerRole(ClientType.LANDLORD);
            crossPlayerRole = ClientType.PEASANT;
        } else {
            super.getPlayer().setRole(ClientType.PEASANT);
            super.setPrevPlayerRole(ClientType.PEASANT);
            super.setNextPlayerRole(ClientType.PEASANT);
            crossPlayerRole = ClientType.LANDLORD;
        }
    }

    public CurrentRoomInfo4P(int roomId, String roomOwner) {
        super(roomId, roomOwner);
    }

    public String getCrossPlayerName() {
        return crossPlayerName;
    }

    public void setCrossPlayerName(String crossPlayerName) {
        this.crossPlayerName = crossPlayerName;
    }

    public ClientType getCrossPlayerRole() {
        return crossPlayerRole;
    }

    public void setCrossPlayerRole(ClientType crossPlayerRole) {
        this.crossPlayerRole = crossPlayerRole;
    }

    public int getCrossPlayerSurplusPokerCount() {
        return crossPlayerSurplusPokerCount;
    }

    public void setCrossPlayerSurplusPokerCount(int crossPlayerSurplusPokerCount) {
        this.crossPlayerSurplusPokerCount = crossPlayerSurplusPokerCount;
    }
}
