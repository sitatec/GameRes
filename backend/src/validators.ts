import bent from "bent";
import { NextFunction, Request, Response } from "express";
import { env } from "process";

const SECRET = (env.CLIENT_ID as string) + env.CLIENT_SECRET;

export const validateRequest = async (request: Request, response: Response, next : NextFunction) => {
  console.log("\nVALIDATE TOKEN CALLED\n")
  if(request.params.secret != SECRET){
    return response.status(400).send("Invalid Secret")
  }
  next();
};
