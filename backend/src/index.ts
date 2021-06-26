import app from "express"
import { env } from "process"
import { Controller } from "./Controller";

const controller = new Controller();

app().get("/apitoken/:secret", (request, response) => {

  if (request.params.secret != env.CLIENT_ID as string + env.CLIENT_SECRET) {
    return response.status(400).send("Invalid Secret")
  }


}).get("/apitoken/:secret/get", controller.getToken)
  .listen(env.PORT || 8080, () => console.log("SERVER STARTED!!"));