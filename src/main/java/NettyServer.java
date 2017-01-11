import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by A-Lugovskoy
 */
public class NettyServer {

    public static void main(String[] args) {
        // 4 threads enough for > 1000000 client.

        ChannelFactory channelFactory = new NioServerSocketChannelFactory(
                //Boss executors thread pool
                new OrderedMemoryAwareThreadPoolExecutor(1, 400000000,
                        2000000000, 60, TimeUnit.SECONDS),
                //Handler executors thread pool
                new OrderedMemoryAwareThreadPoolExecutor(1, 400000000,
                        2000000000, 60, TimeUnit.SECONDS));

        //Standard class in netty for simple open connect.
        ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);

        //Set new ChannelPipelines
        bootstrap.setPipelineFactory(() -> Channels.pipeline(new NettyServerHandler()));

        //Bind channel on localhost:11111
        bootstrap.bind(new InetSocketAddress(11111));

    }

}
