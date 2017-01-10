import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

import java.io.UnsupportedEncodingException;

/**
 * Created by A-Lugovskoy
 */
public class NettyClientHandler extends SimpleChannelHandler {

    //It's handler for send message in network.
    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        //Our message
        String message = (String) e.getMessage();

        //send byte[]
        Channels.write(ctx, e.getFuture(), ChannelBuffers.wrappedBuffer(message.getBytes("UTF-8")),
                e.getRemoteAddress());

        System.out.println(" ---- WriteRequested : -- message: " + message);
    }

    //handler for response server
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        System.out.println(" ---- MessageReceived -- message: " + getStringFromBuffer((ChannelBuffer) e.getMessage()));
    }

    //exceptions
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println(e.getCause());
        e.getChannel().close();
    }

    //build string of buffer.
    public static String getStringFromBuffer(ChannelBuffer channelBufferufer) throws UnsupportedEncodingException {

        int bufSize = channelBufferufer.readableBytes();
        byte [] bytes = new byte[bufSize];
        channelBufferufer.readBytes(bytes);

        return new String(bytes, "UTF-8");
    }


}
