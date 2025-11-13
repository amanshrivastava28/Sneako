import React, { useState, useEffect } from "react";
import axios from "axios";

function ProfileInformation() {
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    phone: "",
    email: "",
    password: "",
  });
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const storedUserString = localStorage.getItem("user");
    const parsedstoredUser = JSON.parse(storedUserString); 
    const token = parsedstoredUser.jwt;

 

    if (parsedstoredUser && token) {
      setUser(parsedstoredUser);
      

    
      axios
        .get(`http://localhost:8081/api/v1/user-service/users/${parsedstoredUser.id}`, {
          headers: {
            Authorization: `Bearer ${parsedstoredUser.jwt}`,
          },
        })
        .then((res) => {
          const userData = res.data;
          console.log("Fetched user data:", userData);
          setFormData({
            name: userData.username,
            phone: userData.phone || "",
            email: userData.email,
            address: userData.address || "",
            password: "",
          });
        })
        .catch((err) => {
          console.error("Error fetching user data:", err);
          setError("Failed to load user data.");
        });
    }
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setMessage("");

const token = user.jwt;
    if (!user || !token) {
      setError("User not logged in.");
      return;
    }

    try {
      const updatedData = {
        id: user.id,
        username: formData.name,
        phone: formData.phone,
        email: formData.email,
        password: formData.password || null,
      };

      const response = await axios.put(
        `http://localhost:8081/api/v1/user-service/users/${user.id}`,
        updatedData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      const updatedUser = {
        ...user,
        ...response.data,
      };

      localStorage.setItem("user", JSON.stringify(updatedUser));
      setUser(updatedUser);
      setMessage("Profile updated successfully!");
      setIsEditing(false);
    } catch (err) {
      console.error("Error updating profile:", err);
      setError("Failed to update profile. Please try again.");
    }
  };

  const handleCancel = () => {
    setIsEditing(false);
    if (user) {
      setFormData({
        name: user.username,
        phone: user.phone || "",
        email: user.email,
        password: "",
      });
    }
  };

  if (!user) {
    return (
      <p className="text-xl text-gray-700 text-center">
        Please log in to manage your profile.
      </p>
    );
  }

  return (
    <div className="p-4 max-w-3xl mx-auto w-full">
      {message && (
        <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4 text-center">
          {message}
        </div>
      )}
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4 text-center">
          {error}
        </div>
      )}

      <div className="flex justify-between items-center border-b pb-4 mb-6">
        <h3 className="text-2xl font-semibold">Personal Information</h3>
        {!isEditing && (
          <button
            onClick={() => setIsEditing(true)}
            className="text-blue-600 hover:text-blue-800 font-medium"
          >
            Edit
          </button>
        )}
      </div>

      {!isEditing ? (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <p className="text-gray-500 text-sm">Full Name</p>
            <p className="font-medium text-gray-800">{formData.name}</p>
          </div>
          <div>
            <p className="text-gray-500 text-sm">Email</p>
            <p className="font-medium text-gray-800">{user.email}</p>
          </div>
          <div>
            <p className="text-gray-500 text-sm">Phone</p>
            <p className="font-medium text-gray-800">{user.phone || "Not provided"}</p>
          </div>
        </div>
      ) : (
        <form onSubmit={handleSubmit}>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Full Name
              </label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                className="w-full border px-3 py-2 rounded"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Email
              </label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                className="w-full border px-3 py-2 rounded"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Phone
              </label>
              <input
                type="text"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
                className="w-full border px-3 py-2 rounded"
              />
            </div>
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                New Password
              </label>
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                className="w-full border px-3 py-2 rounded"
                placeholder="Leave blank to keep current"
              />
            </div>
          </div>
          <div className="flex justify-end mt-6 space-x-4">
            <button
              type="button"
              onClick={handleCancel}
              className="px-4 py-2 border rounded text-gray-700 hover:bg-gray-100"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              Save Changes
            </button>
          </div>
        </form>
      )}
    </div>
  );
}

export default ProfileInformation;
