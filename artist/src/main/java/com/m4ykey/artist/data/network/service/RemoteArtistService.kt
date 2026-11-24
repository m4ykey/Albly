package com.m4ykey.artist.data.network.service

import com.m4ykey.artist.data.network.dto.ArtistDto
import com.m4ykey.artist.data.network.dto.ArtistReleaseDto

interface RemoteArtistService {

    suspend fun getArtistById(id : Int) : ArtistDto
    suspend fun getArtistReleases(id : Int, page : Int, perPage : Int) : ArtistReleaseDto

}