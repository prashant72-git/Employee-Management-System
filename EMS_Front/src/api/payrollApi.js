import client from "./axiosClient";

// Correct: send month as query param
export const previewPayroll = (month) =>
  client.post(`/payroll/runs/preview`, null, { params: { month } });

export const lockPayroll = (month) =>
  client.post(`/payroll/runs/${month}/lock`);
