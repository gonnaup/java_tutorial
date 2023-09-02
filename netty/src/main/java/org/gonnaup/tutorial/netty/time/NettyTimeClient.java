package org.gonnaup.tutorial.netty.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * @author gonnaup
 * @version created at 2023/3/20 下午5:11
 */
public class NettyTimeClient {


    private static final Logger logger = LoggerFactory.getLogger(NettyTimeClient.class);


    public void startClient(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            group.shutdownGracefully();
        }
    }

    static class TimeClientHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            logger.info("channel actived...");
            byte[] msg = ("QUERY TIME $" + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8);
            ByteBuf buf = Unpooled.buffer(msg.length);
            buf.writeBytes(msg);
            ctx.writeAndFlush(buf);
//            super.channelActive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf body = (ByteBuf) msg;
            byte[] reqBytes = new byte[body.readableBytes()];
            body.readBytes(reqBytes);
            String message = new String(reqBytes, StandardCharsets.UTF_8);
            logger.info("收到服务器时间为: {}", message);
            ctx.close();
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new NettyTimeClient().startClient("localhost", NettyTimeServer.PORT);
        }
    }
}
