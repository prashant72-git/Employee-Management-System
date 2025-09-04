import React, { useState } from "react";
import { previewPayroll, lockPayroll } from "../api/payrollApi";

export default function PayrollPage() {
  const [month, setMonth] = useState(() => {
    const d = new Date();
    return d.getFullYear() + "-" + String(d.getMonth() + 1).padStart(2, "0");
  });
  const [preview, setPreview] = useState(null);
  const [lockedId, setLockedId] = useState(null);

  const doPreview = async () => {
    try {
      const res = await previewPayroll(month);
      setPreview(res.data);
      setLockedId(null);
    } catch (e) {
      console.error(e);
      alert("Preview failed: " + (e.response?.data?.message || e.message));
    }
  };

  const doLock = async () => {
    if (!preview) {
      alert("Please preview payroll first.");
      return;
    }
    if (!confirm("Lock payroll for " + month + " ?")) return;

    try {
      const res = await lockPayroll(month);
      setLockedId(res.data); // backend returns Long id
      alert("Payroll locked successfully (Run ID: " + res.data + ")");
    } catch (e) {
      console.error(e);
      alert("Lock failed: " + (e.response?.data?.message || e.message));
    }
  };

  return (
    <div>
      <div className="card mb-4 p-4">
        <h3 className="font-semibold mb-2">Payroll Processing</h3>
        <div className="flex items-center gap-2">
          <input
            type="month"
            className="border p-2 rounded"
            value={month}
            onChange={(e) => setMonth(e.target.value)}
          />
          <button className="btn btn-primary" onClick={doPreview}>
            Preview
          </button>
          <button className="btn" onClick={doLock}>
            Lock
          </button>
        </div>
      </div>

      {preview && (
        <div className="card p-4">
          <h4 className="font-semibold mb-2">
            Preview - {preview.month} ({preview.status})
          </h4>
          <div className="overflow-x-auto">
            <table className="w-full text-left border">
              <thead className="bg-gray-100">
                <tr>
                  <th className="p-2">ID</th>
                  <th className="p-2">Employee</th>
                  <th className="p-2">Present</th>
                  <th className="p-2">Unpaid</th>
                  <th className="p-2">Gross</th>
                  <th className="p-2">Net</th>
                </tr>
              </thead>
              <tbody>
                {preview.items.map((it) => (
                  <tr key={it.employeeId} className="border-t">
                    <td className="p-2">{it.employeeId}</td>
                    <td className="p-2">{it.employeeCode}</td>
                    <td className="p-2">{it.presentDays}</td>
                    <td className="p-2">{it.unpaidDays}</td>
                    <td className="p-2">{it.gross}</td>
                    <td className="p-2">{it.net}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
          {lockedId && (
            <div className="mt-3 text-green-600">
              ✅ Payroll Locked (Run ID: {lockedId})
            </div>
          )}
        </div>
      )}
    </div>
  );
}
