package org.koitharu.kotatsu.parsers.site.madara.id

import org.koitharu.kotatsu.parsers.MangaLoaderContext
import org.koitharu.kotatsu.parsers.MangaSourceParser
import org.koitharu.kotatsu.parsers.model.MangaParserSource
import org.koitharu.kotatsu.parsers.site.madara.MadaraParser

@MangaSourceParser("HWAGO", "Hwago", "id")
internal class Hwago(context: MangaLoaderContext) :
	MadaraParser(context, MangaParserSource.HWAGO, "01.hwago.xyz") {
	override val listUrl = "komik/"
	override val tagPrefix = "genre/"
	override val datePattern = "d MMMM yyyy"

	override suspend fun getDetails(manga: org.koitharu.kotatsu.parsers.model.Manga): org.koitharu.kotatsu.parsers.model.Manga {
		val details = super.getDetails(manga)
		// Clean up bad prefixes added by the site in the detail page
		val cleanTitle = details.title.replace(Regex("^(MANHWA|MANHUA|HOT|NEW|COMPLETED)\\s+", RegexOption.IGNORE_CASE), "").trim()
		return details.copy(title = cleanTitle)
	}

	override suspend fun getPages(chapter: org.koitharu.kotatsu.parsers.model.MangaChapter): List<org.koitharu.kotatsu.parsers.model.MangaPage> {
		val pages = super.getPages(chapter)
		// Fix double slashes in image URLs (e.g., .site//chapter-1/) which cause 404s on their CDN
		return pages.map { page ->
			page.copy(url = page.url.replace(Regex("(?<!:)//"), "/"))
		}
	}
}
