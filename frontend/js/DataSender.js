function sendRequest(method, url, body) {
  const headers = {
    "Content-Type": "application/json",
  };
  return fetch(url, {
    method: method,
    body: JSON.stringify(body),
    headers: headers,
  }).then((response) => {
    if (response.ok) {
      window.open("checkMail.html");
      return response.json();
    }
    return response.json().then((error) => {
      const e = new Error("fail");
      e.data = error;
      throw e;
    });
  });
}
