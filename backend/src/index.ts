import app from "express"
import { env } from "process"
import { Controller } from "./Controller";
import { validateRequest } from "./validators";

const controller = new Controller();

app()
  .get("/apitoken/:secret", validateRequest)
  .get("/apitoken/:secret/get", controller.getToken)
  .listen(env.PORT || 8080, () => console.log("SERVER STARTED!!"));