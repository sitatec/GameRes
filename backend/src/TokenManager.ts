import { env } from "process";
import bent from "bent";

export class TokenManager {

  private readonly ACCESS_TOKEN_VALIDATION_URL = "https://id.twitch.tv/oauth2/validate";
  private readonly ACCESS_TOKEN_URL =
    "https://id.twitch.tv/oauth2/token?client_id=" +
    `${env.CLIENT_ID}&client_secret=${env.CLIENT_SECRET}&grant_type=client_credentials`;

  constructor(private readonly _httpClient = bent("json"), private _token = "") { }

  public get token() {
    return this._token;
  }

  public generateNewToken() {
    return this._httpClient(this.ACCESS_TOKEN_URL).then((response) => {
      this._token = response.access_token;
    })
  }

  public async isCurrentTokenValid(): Promise<boolean> {
    if (!this._token) return false;

    const headers = new Headers();
    headers.append("Authorization", `OAuth ${this._token}`);

    return this._httpClient(this.ACCESS_TOKEN_VALIDATION_URL, headers)
      .then(() => true)
      .catch(() => false);
  }

}