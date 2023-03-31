package com.niit.usertrackservice.service;

import com.niit.rabbitmq.domain.UserDTO;
import com.niit.usertrackservice.config.Producer;
import com.niit.usertrackservice.domain.Movie;
import com.niit.usertrackservice.domain.User;
import com.niit.usertrackservice.exception.MovieNotFoundException;
import com.niit.usertrackservice.exception.UserAlreadyExistsException;
import com.niit.usertrackservice.exception.UserNotFoundException;
import com.niit.usertrackservice.repository.UserMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
@Service
public class UserMovieServiceImpl implements UserMovieService{
    private UserMovieRepository userMovieRepository;
    @Autowired
    Producer producer;
    @Autowired
    public UserMovieServiceImpl(UserMovieRepository userMovieRepository) {
        this.userMovieRepository = userMovieRepository;
    }

    @Override
    public User registerUser(User user) throws UserAlreadyExistsException {

        UserDTO userdto=new UserDTO();
        userdto.setUserName(user.getUserName());
        userdto.setEmail(user.getEmail());
        userdto.setPassword(user.getPassword());
        if(userMovieRepository.findById(user.getEmail()).isPresent())
        {
            throw new UserAlreadyExistsException();
        }
        else{
            userMovieRepository.save(user);
            System.out.println("saved user in mongo");
            producer.sendMessageToRabbitMq(userdto);
        }
        return user;
    }

    @Override
    public User saveUserMovieToList(Movie movie, String email) throws UserNotFoundException {
        if(userMovieRepository.findById(email).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user = userMovieRepository.findByEmail(email);
        if(user.getMovieList() == null)
        {
            user.setMovieList(Arrays.asList(movie));
        }
        else {
            List<Movie> movies = user.getMovieList();
            movies.add(movie);
            user.setMovieList(movies);
        }
        return userMovieRepository.save(user);
    }

    @Override
    public User deleteUserMovieFromList(String email, String movieId) throws UserNotFoundException, MovieNotFoundException {
        boolean movieIdIsPresent = false;
        if(userMovieRepository.findById(email).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user = userMovieRepository.findById(email).get();
        List<Movie> movies = user.getMovieList();
        movieIdIsPresent = movies.removeIf(x->x.getMovieId().equals(movieId));
        if(!movieIdIsPresent)
        {
            throw new MovieNotFoundException();
        }
        user.setMovieList(movies);
        return userMovieRepository.save(user);
    }

    @Override
    public List<Movie> getAllUserMovies(String email) throws UserNotFoundException {
        if(userMovieRepository.findById(email).isEmpty())
        {
            throw new UserNotFoundException();
        }
        return userMovieRepository.findById(email).get().getMovieList();
    }

    @Override
    public List<Movie> getAllMoviesByGenre(String genre) {
      return null;
    }

    @Override
    public List<Movie> getAllMovies() {
        return null;
    }
}
