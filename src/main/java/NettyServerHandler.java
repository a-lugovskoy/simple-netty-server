import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;

/**
 * Created by A-Lugovskoy
 * Server handlers
 */
public class NettyServerHandler extends SimpleChannelHandler {


    //It's handler for send message in network.
    @Override
    public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) throws Exception {

        String message = (String) e.getMessage();

        Channels.write(ctx, e.getFuture(), ChannelBuffers.wrappedBuffer(message.getBytes("UTF-8")),
                e.getRemoteAddress());
    }

    //handler for response client
    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        String s = NettyClientHandler.getStringFromBuffer(((ChannelBuffer)e.getMessage()));
        e.getChannel().write("Response from server: " + s);
        System.out.println("Server got message : " + s);
    }

    //exception handler for exceptions in network
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        System.out.println("Exception" + e.getCause());
        e.getChannel().close();
    }
}
