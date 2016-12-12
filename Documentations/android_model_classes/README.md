### Model Class for the Android Client ###

#### Following Classes will be needed for the android client, e.g. when using list view or showing details of a certain model e.g. building
```javascript
User {
	id: long,
	email: string, (unique) (dont return when request for list of notes etc.)
	password: string,
	nickname: string,
	major: string,
	groups: [] of Group (dont return on user request),
	notes: [] of Note (dont return on request for just user),
	//ab hier optional
	university: string,
	foto: local with string path
}

Building
	address: string,
	city: string,
	country: string,
	imageUrl: string
	rooms: [] of Room (dont return when request for buildings)
}

Room {
	name: string,
	available: bool //if true green else nothing or red
	
	//server side
	taken: {
		days: [
			tag: "mo",
			times: [
				{
					from: 10,
					to: 12
				},
				{
					from: 12,
					to: 14
				}
			]
		]
	}
}

Group {
	name: string,
	description: string,
	members: [] of string,
	notes: [] of Note,
	password: string,
	isPublic: bool
}

Note {
	course: string,
	title: string,
	content: string,
	creator: nickname of User,
	creationDate: timestamp,
	rating: int //+1 or -1 for every user on each note
}
```

