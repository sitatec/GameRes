import { Request, Response } from "express";
import { env } from "process";

const SECRET = (env.CLIENT_ID as string) + env.CLIENT_SECRET;

export const validateRequest = async (request: Request, response: Response) => {
  if(request.params.secret != SECRET){
    return response.status(400).send("Invalid Secret")
  }
}
