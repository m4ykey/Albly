package com.m4ykey.artist.data.service

import com.m4ykey.artist.data.dto.ArtistDto

interface RemoteArtistService {

    suspend fun getArtistById(id : Int) : ArtistDto

}