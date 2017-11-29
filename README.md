# Mongo Parameter Service #

## Service purpose ##

### Available REST endpoints ###

Once service started API description can be found on [Swagger](https://swagger.io/) page by URI `/swagger-ui.html`.

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
