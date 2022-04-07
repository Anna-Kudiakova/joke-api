package com.joke_api.client;


import com.joke_api.client.dao.impl.UserRepositoryImpl;
import com.joke_api.client.dao.model.Categories;
import com.joke_api.client.dao.model.Flags;
import com.joke_api.client.dao.model.User;
import com.joke_api.client.services.CustomJokeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomJokeServiceTest {

    @Mock
    private UserRepositoryImpl userRepository;

    @InjectMocks
    private CustomJokeService customJokeService;

    @Test
    void testGetCategories_isOk() {
        Categories categories = Categories.builder()
                .programming(false)
                .misc(true)
                .dark(true)
                .pun(false)
                .spooky(false)
                .christmas(true)
                .build();
        User user = User.builder()
                .username("Christine-Marie")
                .categories(categories)
                .build();
        String username = "Christine-Marie";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        List<String> expectedList = List.of("Misc", "Dark", "Christmas");
        List<String> actualList = customJokeService.getCategories(username);

        assertEquals(expectedList, actualList);

    }

    @Test
    void testGetFlags_isOk() {
        Flags flags = Flags.builder()
                .nsfw(true)
                .religious(true)
                .political(true)
                .racist(false)
                .sexist(true)
                .explicit(false)
                .build();
        User user = User.builder()
                .username("Christine-Marie")
                .flags(flags)
                .build();
        String username = "Christine-Marie";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        List<String> expectedList = List.of("nsfw", "religious", "political", "sexist");
        List<String> actualList = customJokeService.getFlags(username);

        assertEquals(expectedList, actualList);

    }
}
