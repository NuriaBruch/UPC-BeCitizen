# Guía supervivencia

## Poner en marcha el servidor por primera vez

Para poner el marcha el servidor són necesarias dos cosas: Node v9 y npm v5, que se pueden descargar desde: https://nodejs.org/es/download/

Hecho esto, podemos ir a la carpeta dosde tenemos el servidor, entrar en ella y realizar los comandos de abajo para instalar las librerías:

    sudo npm install

Si por algún caso hay algún error, hacemos:

    rm package-lock.json
    sudo npm install

En teoría todo va bien, tenemos los paquetes, solo falta configurar la base de datos.

## Configurar la base de datos

Debemos entrar en la carpeta ./config y copiar el archivo **local.js.template** para renombrarlo a **local.js** . Este archivo nos permitirá configurar nuestra base de datos y teniendo en cuenta que está tendró de .gitignore, cada uno podemos tener nuestras contraseñas por separado.

El json de local.js es muy intuitivo y solo hace falta rellenar nuestras credenciales con vuestra base de datos **postgres** que tenéis que tener instalada.

Muy bien, hecho esto, ahora podeis ejecutar:

    npm start

y el servidor debería estar funcionando. (Si teneis sails instalado también podéis hacer **sails lift**)

## No quiero/tengo postgres

Hay una forma sencilla de tener el servidor funcionando sin tener postgress. Solo tienes que ir a ./config/models.js y cambiar: **postgresDB** por **localDiskDb**, quedando (la línea cambiada) así:

```connection: 'localDiskDb', ```

Eso hará que nuestra DB sea un json y podemos hacer npm start

## Quiero ver la DB 

Si tienes la DB en postgress puedes usar pgadmin y conectarte, aunque hay otra forma, y es una vez teniendo el servidor en marcha (suponiendo que no has tocado el puerto), podemos acceder al navegador para hacer consultas del tipo:

    http://localhost:1337/user

y se nos debería devolver un json con las filas de la tabla 'user'.

Para más info: https://sailsjs.com/documentation/concepts/blueprints


