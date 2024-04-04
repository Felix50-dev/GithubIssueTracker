package com.example.githubissuetracker.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.example.githubissuetracker.data.ApolloIssuesClient
import com.example.githubissuetracker.domain.GetIssuesUseCase
import com.example.githubissuetracker.domain.IssueClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl("https://api.github.com/graphql")
            .addHttpInterceptor(AuthorizationInterceptor(""))
            .build()
    }

    @Provides
    @Singleton
    fun provideIssuesClient(apolloClient: ApolloClient): IssueClient {
        return ApolloIssuesClient(apolloClient)
    }

    @Provides
    @Singleton
    fun provideGetIssuesUseCase(issuesClient: IssueClient): GetIssuesUseCase {
        return GetIssuesUseCase(issuesClient)
    }

    class AuthorizationInterceptor(val token: String) : HttpInterceptor {
        override suspend fun intercept(
            request: HttpRequest,
            chain: HttpInterceptorChain
        ): HttpResponse {
            return chain.proceed(
                request.newBuilder().addHeader("Authorization", "Bearer $token").build()
            )
        }
    }
}