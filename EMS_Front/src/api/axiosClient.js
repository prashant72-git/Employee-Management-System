import axios from "axios";

const client = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: { "Content-Type": "application/json" },
  timeout: 10000
});

export default client;
