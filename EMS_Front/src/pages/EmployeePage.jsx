import React, { useEffect, useState } from "react";
import {
  getEmployees,
  createEmployee,
  deleteEmployee,
  updateEmployee,
} from "../api/employeeApi";

export default function EmployeePage() {
  const [employees, setEmployees] = useState([]);
  const [form, setForm] = useState(initForm());
  const [editingId, setEditingId] = useState(null);
  const [viewEmployee, setViewEmployee] = useState(null);

  useEffect(() => {
    load();
  }, []);

  function initForm() {
    return {
      code: "",
      firstName: "",
      lastName: "",
      email: "",
      phone: "",
      department: "",
      designation: "",
      dateOfJoining: "",
      status: "ACTIVE",
      baseSalary: 0,
      bankAccountNo: "",
    };
  }

  const load = async () => {
    try {
      const res = await getEmployees();
      setEmployees(res.data || []);
    } catch (e) {
      console.error(e);
      alert("Failed to load employees");
    }
  };

  const handleCreateOrUpdate = async () => {
    try {
      if (editingId) {
        await updateEmployee(editingId, form);
        setEditingId(null);
      } else {
        await createEmployee(form);
      }
      setForm(initForm());
      load();
    } catch (e) {
  console.error(e);
  alert("Save failed: " + (e.response?.data?.message || JSON.stringify(e.response?.data) || e.message));
}
  };

  const handleDelete = async (id) => {
    if (!confirm("Delete employee?")) return;
    try {
      await deleteEmployee(id);
      load();
    } catch (e) {
      console.error(e);
      alert("Delete failed");
    }
  };

  return (
    <div className="p-4 space-y-6">
      {/* CREATE / UPDATE FORM */}
      <div className="card p-4 shadow rounded-lg">
        <h3 className="font-semibold mb-2">
          {editingId ? "Edit employee" : "Add employee"}
        </h3>
        <div className="grid grid-cols-2 gap-2">
          <input
            placeholder="Code"
            className="border p-2"
            value={form.code}
            onChange={(e) => setForm({ ...form, code: e.target.value })}
          />
          <input
            placeholder="First name"
            className="border p-2"
            value={form.firstName}
            onChange={(e) => setForm({ ...form, firstName: e.target.value })}
          />
          <input
            placeholder="Last name"
            className="border p-2"
            value={form.lastName}
            onChange={(e) => setForm({ ...form, lastName: e.target.value })}
          />
          <input
            placeholder="Email"
            className="border p-2"
            value={form.email}
            onChange={(e) => setForm({ ...form, email: e.target.value })}
          />
          <input
            placeholder="Phone"
            className="border p-2"
            value={form.phone}
            onChange={(e) => setForm({ ...form, phone: e.target.value })}
          />
          <input
            placeholder="Department"
            className="border p-2"
            value={form.department}
            onChange={(e) => setForm({ ...form, department: e.target.value })}
          />
          <input
            placeholder="Designation"
            className="border p-2"
            value={form.designation}
            onChange={(e) => setForm({ ...form, designation: e.target.value })}
          />
          <input
            type="date"
            className="border p-2"
            value={form.dateOfJoining}
            onChange={(e) =>
              setForm({ ...form, dateOfJoining: e.target.value })
            }
          />
          <select
            className="border p-2"
            value={form.status}
            onChange={(e) => setForm({ ...form, status: e.target.value })}
          >
            <option value="ACTIVE">Active</option>
            <option value="INACTIVE">Inactive</option>
            <option value="TERMINATED">Terminated</option>
          </select>
          <input
            placeholder="Base salary"
            type="number"
            className="border p-2"
            value={form.baseSalary}
            onChange={(e) =>
              setForm({ ...form, baseSalary: Number(e.target.value) })
            }
          />
          <input
            placeholder="Bank Account No"
            className="border p-2"
            value={form.bankAccountNo}
            onChange={(e) =>
              setForm({ ...form, bankAccountNo: e.target.value })
            }
          />
        </div>
        <div className="mt-2 space-x-2">
          <button className="btn btn-primary" onClick={handleCreateOrUpdate}>
            {editingId ? "Update" : "Create"}
          </button>
          {editingId && (
            <button
              className="btn"
              onClick={() => {
                setEditingId(null);
                setForm(initForm());
              }}
            >
              Cancel
            </button>
          )}
        </div>
      </div>

      {/* EMPLOYEE LIST */}
      <div className="card p-4 shadow rounded-lg">
        <h3 className="font-semibold mb-2">Employees</h3>
        <ul>
          {employees.map((emp) => (
            <li
              key={emp.id}
              className="flex justify-between items-center py-2 border-b"
            >
              <div>
                <div className="font-medium">
                  {emp.firstName} {emp.lastName}{" "}
                  <span className="text-sm text-gray-500">({emp.code})</span>
                </div>
                <div className="text-sm text-gray-600">
                  {emp.email} • {emp.department || "—"} •{" "}
                  {emp.designation || "—"}
                </div>
              </div>
              <div className="space-x-2">
                <button className="btn" onClick={() => setViewEmployee(emp)}>
                  View
                </button>
                <button
                  className="btn"
                  onClick={() => {
                    setEditingId(emp.id);
                    setForm(emp);
                  }}
                >
                  Edit
                </button>
                <button
                  className="btn btn-danger"
                  onClick={() => handleDelete(emp.id)}
                >
                  Delete
                </button>
              </div>
            </li>
          ))}
        </ul>
      </div>

      {/* VIEW DETAILS MODAL */}
      {viewEmployee && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
          <div className="bg-white p-6 rounded-lg w-1/2">
            <h3 className="text-lg font-semibold mb-4">
              Employee Details ({viewEmployee.code})
            </h3>
            <div className="grid grid-cols-2 gap-2 text-sm">
              <p><b>Name:</b> {viewEmployee.firstName} {viewEmployee.lastName}</p>
              <p><b>Email:</b> {viewEmployee.email}</p>
              <p><b>Phone:</b> {viewEmployee.phone}</p>
              <p><b>Department:</b> {viewEmployee.department}</p>
              <p><b>Designation:</b> {viewEmployee.designation}</p>
              <p><b>Date of Joining:</b> {viewEmployee.dateOfJoining}</p>
              <p><b>Status:</b> {viewEmployee.status}</p>
              <p><b>Salary:</b> {viewEmployee.baseSalary}</p>
              <p><b>Bank A/c:</b> {viewEmployee.bankAccountNo}</p>
              <p><b>Created At:</b> {viewEmployee.createdAt}</p>
              <p><b>Updated At:</b> {viewEmployee.updatedAt}</p>
            </div>
            <div className="mt-4 text-right">
              <button
                className="btn"
                onClick={() => setViewEmployee(null)}
              >
                Close
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
