package com.quinto.comicbook.data.repository.dto

import java.util.Date

@Suppress("SpellCheckingInspection")

data class ComicDto(
    val id: Int?,
    val title: String?,
    val issueNumber: Double?,
    val description: String?,
    val modified: Date?,
    val thumbnail: ThumbnailDto?,
    val dates: List<DateDto>? = null,
)

data class DateDto(
    val type: String?,
    val date: Date?
)

data class ThumbnailDto(
    val path: String?,
    val extension: String?
)

/*
{
  "code": 200,
  "status": "Ok",
  "copyright": "© 2024 MARVEL",
  "attributionText": "Data provided by Marvel. © 2024 MARVEL",
  "attributionHTML": "\u003Ca href=\"http://marvel.com\"\u003EData provided by Marvel. © 2024 MARVEL\u003C/a\u003E",
  "etag": "2300248f2a984dbe83cd60320e60e924809f5821",
  "data": {
    "offset": 0,
    "limit": 40,
    "total": 60092,
    "count": 40,
    "results": [
      {
        "id": 114844,
        "digitalId": 0,
        "title": "Strange Academy: Blood Hunt (2024) #1",
        "issueNumber": 1,
        "variantDescription": "",
        "description": "The vampire onslaught of BLOOD HUNT reaches worldwide all the way down to NEW ORLEANS, LOUISIANA, home of the STRANGE ACADEMY! DOYLE DORMAMMU, SHAYLEE, TOTH, ZOE, GERMÁN get embroiled in an adventure that will take them around the Marvel Universe and right into the center of the BLOOD HUNT action! That's right, the kids from the best new series of the decade find themselves at center stage of the Marvel event of 2024!",
        "modified": "2024-01-24T10:23:20-0500",
        "isbn": "",
        "upc": "75960620894400111",
        "diamondCode": "",
        "ean": "",
        "issn": "",
        "format": "Comic",
        "pageCount": 40,
        "textObjects": [],
        "resourceURI": "http://gateway.marvel.com/v1/public/comics/114844",
        "urls": [
          {
            "type": "detail",
            "url": "http://marvel.com/comics/issue/114844/strange_academy_blood_hunt_2024_1?utm_campaign=apiRef&utm_source=25a07f7adccf7328d3153451c26bd992"
          }
        ],
        "series": {
          "resourceURI": "http://gateway.marvel.com/v1/public/series/39279",
          "name": "Strange Academy: Blood Hunt (2024 - Present)"
        },
        "variants": [],
        "collections": [],
        "collectedIssues": [],
        "dates": [
          {
            "type": "onsaleDate",
            "date": "2024-05-08T00:00:00-0400"
          },
          {
            "type": "focDate",
            "date": "2024-04-08T00:00:00-0400"
          }
        ],
        "prices": [
          {
            "type": "printPrice",
            "price": 4.99
          }
        ],
        "thumbnail": {
          "path": "http://i.annihil.us/u/prod/marvel/i/mg/c/d0/65d739cbc5bf5",
          "extension": "jpg"
        },
        "images": [
          {
            "path": "http://i.annihil.us/u/prod/marvel/i/mg/c/d0/65d739cbc5bf5",
            "extension": "jpg"
          }
        ],
        "creators": {
          "available": 6,
          "collectionURI": "http://gateway.marvel.com/v1/public/comics/114844/creators",
          "items": [
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/creators/10172",
              "name": "Vc Clayton Cowles",
              "role": "letterer"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/creators/430",
              "name": "Edgar Delgado",
              "role": "colorist (cover)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/creators/4300",
              "name": "Nick Lowe",
              "role": "editor"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/creators/14277",
              "name": "Daniel Older",
              "role": "writer"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/creators/72",
              "name": "Humberto Ramos",
              "role": "penciler (cover)"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/creators/14367",
              "name": "Luigi Zagaria",
              "role": "inker"
            }
          ],
          "returned": 6
        },
        "characters": {
          "available": 0,
          "collectionURI": "http://gateway.marvel.com/v1/public/comics/114844/characters",
          "items": [],
          "returned": 0
        },
        "stories": {
          "available": 2,
          "collectionURI": "http://gateway.marvel.com/v1/public/comics/114844/stories",
          "items": [
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/250908",
              "name": "cover from Strange Academy: Blood Hunt (2024) #1",
              "type": "cover"
            },
            {
              "resourceURI": "http://gateway.marvel.com/v1/public/stories/250909",
              "name": "story from Strange Academy: Blood Hunt (2024) #1",
              "type": "interiorStory"
            }
          ],
          "returned": 2
        },
        "events": {
          "available": 0,
          "collectionURI": "http://gateway.marvel.com/v1/public/comics/114844/events",
          "items": [],
          "returned": 0
        }
      },
      ...
    ]
  }
}
 */