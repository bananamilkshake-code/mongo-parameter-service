# Mongo Parameter Service #

## Service purpose ##

Simple service that allows to create, store and get simple generic parameters. Values of parameter can be described as map which key is a string and value is a primitive type object.<p/>
Values of parameters are separated by `user` (the one who will use parameters of that type) and `validFrom` date (date since when parameter will be applied).<p/>
Aggregation logic is next: from list of values that belong to a `user` a single collection with smallest `validFrom` of those bigger than `date` is selected.

### Available REST endpoints ###

Once service started API description can be found on [Swagger](https://swagger.io/) page by URI `/swagger-ui.html`.

###### POST /parameter/:type ######
Create parameter of `:type`. Parameter description (index and validation description in Mongo style) can be provided.
```
$.ajax({
  type : "POST",
  url: "/parameter/foo",
  contentType: "application/json; charset=utf-8",
  data: `{
  	"index": {},
  	"validation": {}
  }`,
  dataType: "json",
  success : function(r) {
    console.log(r);
  }
});
```

###### POST /parameter/:type/upload/:user?validFrom=:date ######
Upload values to `:type` that will be valid from `:validFrom` date.
```
$.ajax({
  type : "POST",
  url: "/parameter/foo/upload/BAR?validFrom=2017-01-01T00:00:00.000Z",
  contentType: "application/json; charset=utf-8",
  data: `[
    {
	  "height": 1,
	  "width": 10
    },
    {
	  "height": 4,
	  "width": 6
    },
    {
	  "height": 5,
	  "width": 5
    }
  ]`,
  dataType: "json",
  success : function(r) {
    console.log(r);
  }
});
```

###### GET /parameter/:type/:user ######
Retrieves parameter of `:type` for user `:user` that are valid on `:date`.
```
$.ajax({
  type : "GET",
  url: "/parameter/foo/user?date=2017-11-28'T'00:00:00.000Z",
  dataType: "json",
  success : function(r) {
    console.log(r);
  }
});
```

## How do I get set up? ##

As this program uses [MongoDB](https://www.mongodb.com/) as database it is necessay to setup and run it first. You can find an information about setup process in [official Mongo documentation](https://docs.mongodb.com/getting-started/shell/installation/).

Application requires to be installed:
```
mvn clean install
```

To start service go to `parameter-service` catalogue and run Spring application:

```
cd ./parameter-service
mvn spring-boot:run
```

## Who do I talk to? ##
If you have any questions about project feel free to write parameter-service+support@bananamilkshake.me.

## License ##
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
