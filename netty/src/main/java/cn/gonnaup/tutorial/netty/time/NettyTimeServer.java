package cn.gonnaup.tutorial.netty.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author gonnaup
 * @version created at 2023/3/20 下午4:37
 */
public class NettyTimeServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyTimeServer.class);

    static final int PORT = 10010;

    public void startServer(int port) {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(2);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    //option()设置的是服务端用于接收进来的连接，对应boss线程组，SO_BACKLOG为服务端接受连接的队列长度，
                    //如果队列已满，则拒绝连接，默认值win：200,其他：128
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //childOption()设置的是接收的客户端连接的参数，对应worker线程组，
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeServerHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            logger.info("netty time server started...");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    static class TimeServerHandler extends ChannelInboundHandlerAdapter {

        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            ByteBuf receive = (ByteBuf) msg;
            byte[] readBytes = new byte[receive.readableBytes()];
            receive.readBytes(readBytes);
            String body = new String(readBytes, StandardCharsets.UTF_8);
            logger.info("客户端请求信息: {}", body);
            String nowTime = formatter.format(LocalDateTime.now());
            ByteBuf byteBuf = Unpooled.copiedBuffer(nowTime.getBytes(StandardCharsets.UTF_8));
            ctx.write(byteBuf);
            super.channelRead(ctx, msg);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
            super.channelReadComplete(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            logger.error(cause.getMessage());
            ctx.close();
        }
    }

    public static void main(String[] args) {
        new NettyTimeServer().startServer(PORT);
    }
}
