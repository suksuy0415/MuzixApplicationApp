//package com.niit.musicservice.config;
//
//import com.niit.musicservice.domain.Music;
//import com.niit.musicservice.service.MusicServiceImpl;
//import com.niit.rabbitmq.domain.MusicDTO;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
//@Component
//public class Consumer {
//
//    @Autowired
//    private MusicServiceImpl musicService;
//
//    @RabbitListener(queues="user_queue")
//    public void getUserDtoFromRabbitMq(MusicDTO musicDto)
//    {
//        Music music=new Music();
//        music.setEmail(musicDto.getEmail());
//        music.setPassword(musicDto.getPassword());
//        musicService.saveUser(music);
//    }
//}
