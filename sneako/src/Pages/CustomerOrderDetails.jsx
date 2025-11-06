import React, { useState, useEffect } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function CustomerOrderDetails() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchOrders = async () => {
      try {
        setLoading(true);
        setError(null);

        const storedUser = localStorage.getItem("user");
        const parsedUser = storedUser ? JSON.parse(storedUser) : null;
        const userId = parsedUser?.id;
        const token = parsedUser?.jwt;

        if (!userId || !token) {
          setError("User not logged in.");
          setLoading(false);
          return;
        }

        const response = await axios.get(
          `http://localhost:8081/api/v1/order-service/order/user/${userId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        setOrders(response.data);
      } catch (err) {
        console.error("Error fetching orders:", err);
        setError("Failed to fetch order history.");
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, []);

  const handleTrackOrder = (order) => {
    navigate(`/order-tracking/${order.orderId}`, { state: { order } });
  };

  if (loading) {
    return <div className="text-center p-10">Loading order history...</div>;
  }

  if (error) {
    return <div className="text-center p-10 text-red-500">{error}</div>;
  }

  if (orders.length === 0) {
    return (
      <div className="text-center p-10">
        <h2 className="text-2xl font-semibold mb-4">No Orders Found</h2>
        <p className="text-gray-700">
          Your order history is empty. Start shopping now!
        </p>
      </div>
    );
  }

  return (
    <div className="p-4">
      <h2 className="text-2xl font-semibold mb-6">Your Orders</h2>
      <div className="space-y-6">
        {orders.map((order) => (
          <div
            key={order.orderId}
            className="border border-gray-200 rounded-lg p-6 shadow-sm hover:shadow-md transition-shadow duration-300"
          >
            <div className="flex flex-col md:flex-row md:justify-between md:items-center mb-4">
              <div className="mb-2 md:mb-0">
                <p className="font-bold text-lg">Order ID: {order.orderId}</p>
                <p className="text-sm text-gray-600">
                  Date: {new Date(order.orderDate).toLocaleDateString()}
                </p>
              </div>
              <div className="text-right">
                <p className="font-bold text-xl text-blue-600">
                  ₹{order.totalPrice}
                </p>
                <p
                  className={`font-semibold text-sm ${
                    order.orderStatus === "Delivered"
                      ? "text-green-600"
                      : "text-orange-500"
                  }`}
                >
                  Status: {order.orderStatus}
                </p>
              </div>
            </div>
            <hr className="my-4" />
            <div className="space-y-4">
              {order.orderItems.map((item, index) => (
                <div key={index} className="flex items-center space-x-4">
                  <div className="flex-grow">
                    <p className="font-medium">Product ID: {item.productId}</p>
                    <p className="text-sm text-gray-500">
                      Quantity: {item.quantity} | Price: ₹{item.totalPrice}
                    </p>
                  </div>
                </div>
              ))}
            </div>
            <div className="mt-6 text-right">
              <button
                onClick={() => handleTrackOrder(order)}
                className="bg-gray-200 text-gray-800 py-2 px-4 rounded-lg hover:bg-gray-300 transition-colors"
              >
                Track Order
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default CustomerOrderDetails;

 