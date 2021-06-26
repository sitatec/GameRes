import { env } from "process";
import bent from "bent";

export class TokenManager {

  private static readonly ACCESS_TOKEN_VALIDATION_URL = "https://id.twitch.tv/oauth2/validate";
  private static readonly ACCESS_TOKEN_URL =
    "https://id.twitch.tv/oauth2/token?client_id=" +
    `${env.CLIENT_ID}&client_secret=${env.CLIENT_SECRET}&grant_type=client_credentials`;

  constructor(
    private readonly _accessTokenProviderClient = bent(TokenManager.ACCESS_TOKEN_URL, "json", "POST"), 
    private readonly _accessTokenValidatorClien = bent(TokenManager.ACCESS_TOKEN_VALIDATION_URL),
    private _token = "") {}

  public get token() {
    return this._token;
  }

  public generateNewToken() {
    return this._accessTokenProviderClient("").then((response) => {
      this._token = response.access_token;
    })
  }

  public async isCurrentTokenValid(): Promise<boolean> {
    if (!this._token) return false;
    
    const headers = {Authorization: `OAuth ${this._token}`};

    return this._accessTokenValidatorClien("", undefined, headers)
      .then(() => true)
      .catch(() => false);
  }

}