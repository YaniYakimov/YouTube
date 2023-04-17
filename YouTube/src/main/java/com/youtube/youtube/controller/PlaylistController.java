package com.youtube.youtube.controller;

import com.youtube.youtube.model.DTOs.CreatePlaylistDTO;
import com.youtube.youtube.model.DTOs.PlaylistInfoDTO;
import com.youtube.youtube.model.DTOs.PlaylistWithoutOwnerDTO;
import com.youtube.youtube.model.DTOs.UserPlaylistsDTO;
import com.youtube.youtube.service.PlaylistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PlaylistController extends AbstractController{
    @Autowired
    private PlaylistService playlistService;
    @GetMapping("/users/{id}/playlists")
    public UserPlaylistsDTO getUserPlaylists(@PathVariable int id){
        // todo return playlistService.getUserPlaylists(id);
        return null;
    }
    @PostMapping("/playlists")
    public PlaylistInfoDTO createPlaylist(@RequestBody CreatePlaylistDTO createData, HttpSession s){
        int userId=getLoggedId(s);
        // todo
        return playlistService.createPlaylist(userId, createData);
    }
    @PostMapping("/playlists/search")
    public PlaylistWithoutOwnerDTO searchPlaylist(@RequestBody String name){
        // todo return playlistService.searchPlaylist(name);
        return null;
    }

    @PostMapping("/playlists/{playlist-id}/videos")
    public String addVideoToPlaylist(@RequestParam ("playlist-id") int playlistId,@RequestBody int videoId, HttpSession s ){
        int userId=getLoggedId(s);
        // todo return playlistService.addVideoToPlaylist(userId, playlistId, videoId);
        return null;
    }

    @PutMapping("/playlists/{id}")
    public PlaylistInfoDTO editPlaylist(@RequestParam ("id") int playlistId, @RequestBody CreatePlaylistDTO editData, HttpSession s){
        int userId=getLoggedId(s);
        // todo return playlistService.editPlaylist(userId, playlistId, editData);
        return null;
    }

    @DeleteMapping("/playlists/{id}")
    public void deletePlaylist(@RequestParam ("id") int playlistId, HttpSession s){
        int userId=getLoggedId(s);
        //todo playlistService.deletePlaylist(userId, playlistId);

    }

}
