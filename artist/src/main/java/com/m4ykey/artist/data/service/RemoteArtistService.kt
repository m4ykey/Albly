package com.m4ykey.artist.data.service

import com.m4ykey.artist.data.dto.ArtistDto
import com.m4ykey.artist.data.dto.ArtistReleaseDto

interface RemoteArtistService {

    suspend fun getArtistById(id : Int) : ArtistDto
    suspend fun getArtistReleases(id : Int, page : Int, perPage : Int) : ArtistReleaseDto

}