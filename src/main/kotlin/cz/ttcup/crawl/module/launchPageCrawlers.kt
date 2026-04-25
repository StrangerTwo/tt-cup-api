package cz.ttcup.crawl.module

import io.ktor.server.application.*

fun Application.launchPageCrawlers() {
    launchOutdatedPlacesCrawler()

    launchOngoingTournamentsCrawler()
}

