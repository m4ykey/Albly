package com.m4ykey.track.use_case

import com.m4ykey.track.domain.repository.TrackRepository
import com.m4ykey.track.domain.use_case.DeleteTracksUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DeleteTracksUseCaseTest {

    private val repository = mockk<TrackRepository>(relaxed = true)
    private val useCase = DeleteTracksUseCase(repository)

    @Test
    fun whenInvokeIsCalledShouldCallRepositoryDeleteWithCorrectID() = runTest {
        val trackId = "123"

        useCase.invoke(trackId)

        coVerify(exactly = 1) { repository.deleteTracksById(trackId) }
    }

}