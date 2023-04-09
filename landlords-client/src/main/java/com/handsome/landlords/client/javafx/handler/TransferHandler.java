package com.handsome.landlords.client.javafx.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import com.handsome.landlords.channel.ChannelUtils;
import com.handsome.landlords.entity.ClientTransferData.ClientTransferDataProtoc;
import com.handsome.landlords.enums.ClientEventCode;
import com.handsome.landlords.enums.ServerEventCode;
import com.handsome.landlords.print.SimplePrinter;
import com.handsome.landlords.client.javafx.listener.ClientListener;
import com.handsome.landlords.client.javafx.listener.ClientListenerUtils;


public class TransferHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ClientTransferDataProtoc clientTransferData = (ClientTransferDataProtoc) msg;

            if (clientTransferData.getInfo() != null && !clientTransferData.getInfo().isEmpty()) {
                SimplePrinter.printNotice(clientTransferData.getInfo());
            }

            ClientEventCode code = ClientEventCode.valueOf(clientTransferData.getCode());

            SimplePrinter.printNotice("接受服务端信息, 编码："+code+",数据："+clientTransferData.getData()+".");

            if (code != null) {
                ClientListener listener = ClientListenerUtils.getListener(code);

                if (listener != null) {
                    listener.handle(ctx.channel(), clientTransferData.getData());
                } else {
                    SimplePrinter.printNotice("未知的消息编码 "+code+"，忽略该条消息： "+clientTransferData.getData());
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                ChannelUtils.pushToServer(ctx.channel(), ServerEventCode.CODE_CLIENT_HEAD_BEAT, "heartbeat");
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }
}
