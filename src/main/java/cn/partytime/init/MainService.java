package cn.partytime.init;

import cn.partytime.server.ClientServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

@Service
public class MainService {
    private static final Logger logger = LoggerFactory.getLogger(MainService.class);

    @Autowired
    private ClientServer clientServer;


    @Resource(name = "threadPoolTaskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 启动系统加载项目
     */
    @PostConstruct
    public void init() {
        //启动netty服务
        startNettyServer();
        //TODO:启动client1连接远程server

        //TODO:启动client2连接Javaclient

        //TODO:加载本地资源
    }

    /**
     * 启动netty Server
     */
    private void startNettyServer(){
        try {
            logger.info("启动netty主线程");
            threadPoolTaskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    clientServer.nettyStart();
                }
            });
        }catch (Exception e){
            logger.error("线程启动异常:{}",e.getMessage());
        }
    }


    /**
     * 启动第一个客户端连接远程server
     */
    private void startClinetFirstToClientServer(){}

    /**
     * 启动第二个客户端连接远程server
     */
    private void startClinetSecondToJavaClient(){}


    /**
     * 加载本地资源
     */
    private void initLocalResource(){

    }
}
