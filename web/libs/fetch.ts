export class HttpError extends Error {
  status?: number;
  statusText?: string;
  body?: any;

  constructor(status: number, statusText: string, message: string, body: any) {
    super(statusText);
    this.status = status;
    this.statusText = statusText;
    this.message = message;
    this.body = body;
    Object.setPrototypeOf(this, HttpError.prototype);
  }
}

export default async function fetcher<JSON = any>(
  input: RequestInfo,
  init?: RequestInit
): Promise<JSON> {

  const res = await fetch(input, init);

  if (res.ok) {
    const data = await res.text();
    const jsonData = data ? JSON.parse(data) : ({data: {}, message: "", code: 200} as JSON);

    if (jsonData.code !== 200) {
      throw new HttpError(jsonData.status, jsonData.message, jsonData.message, jsonData);
    }

    return jsonData.data;
  } else {
    throw new HttpError(res.status, res.statusText, res.statusText, await res.json());
  }
}
