package com.handsome.landlords.server.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientSide;
import com.handsome.landlords.entity.ServerTransferData.ServerTransferDataProtoc;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ClientRole;
import com.handsome.landlords.enums.ClientStatus;
import com.handsome.landlords.enums.ServerEventCode;
import com.handsome.landlords.print.SimplePrinter;
import com.handsome.landlords.server.ServerContains;
import com.handsome.landlords.server.event.ServerEventListener;

public class ProtobufTransferHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(getId(ctx.channel()));
		SimplePrinter.serverLog("client " + client.getId() + "(" + client.getNickname() + ") disconnected");
		clientOfflineEvent(ctx.channel());
		ctx.channel().close();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		Channel ch = ctx.channel();

		//init client info
		ClientSide clientSide = new ClientSide(getId(ctx.channel()), ClientStatus.TO_CHOOSE, ch);
		clientSide.setNickname(String.valueOf(clientSide.getId()));
		clientSide.setRole(ClientRole.PLAYER);

		ServerContains.CLIENT_SIDE_MAP.put(clientSide.getId(), clientSide);
		SimplePrinter.serverLog("Has client connect to the server: " + clientSide.getId());

		ChannelUtils.pushToClient(ch, ClientEventCode.CODE_CLIENT_CONNECT, String.valueOf(clientSide.getId()));
		ChannelUtils.pushToClient(ch, ClientEventCode.CODE_CLIENT_NICKNAME_SET, null);
	}


	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ServerTransferDataProtoc) {
			ServerTransferDataProtoc serverTransferData = (ServerTransferDataProtoc) msg;
			ServerEventCode code = ServerEventCode.valueOf(serverTransferData.getCode());
			if (code != ServerEventCode.CODE_CLIENT_HEAD_BEAT) {
				ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(getId(ctx.channel()));
				SimplePrinter.serverLog(client.getId() + " | " + client.getNickname() + " do:" + code.getMsg());
				ServerEventListener.get(code).call(client, serverTransferData.getData());
			}
		}
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				try {
					clientOfflineEvent(ctx.channel());
					ctx.channel().close();
				} catch (Exception ignore) {
				}
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
	}

	private int getId(Channel channel) {
		String longId = channel.id().asLongText();
		Integer clientId = ServerContains.CHANNEL_ID_MAP.get(longId);
		if (null == clientId) {
			clientId = ServerContains.getClientId();
			ServerContains.CHANNEL_ID_MAP.put(longId, clientId);
		}
		return clientId;
	}

	private void clientOfflineEvent(Channel channel) {
		int clientId = getId(channel);
		ClientSide client = ServerContains.CLIENT_SIDE_MAP.get(clientId);
		if (client != null) {
			SimplePrinter.serverLog("Has client exit to the server：" + clientId + " | " + client.getNickname());
			ServerEventListener.get(ServerEventCode.CODE_CLIENT_OFFLINE).call(client, null);
		}
	}
}
