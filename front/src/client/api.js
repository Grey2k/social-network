// import cookie from "react-cookies";

const buildHeaders = () => {

  return {
    'Authorization': extractToken()
  }
};

const extractToken = () => {
  return 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmciLCJ1c2VyX2lkIjoiYWNkODMzN2QtNjgxNS00NWMxLWJkZDYtMDI3ZGIzMmM5NDNmIiwiZmlyc3RfbmFtZSI6InN0cmluZyIsImxhc3RfbmFtZSI6InN0cmluZyIsImlhdCI6MTYxMTQyMTc2MSwiZXhwIjoxNjEyNDIxNzYxfQ.s9bTQZ147NYIK40wc4UsgRX44207j5oVvXnOV9GAaSg';
};

export const get = url => {
  return fetch(url, {
    method: 'GET',
    headers: buildHeaders()
  }).then((response) => response.json());
};
