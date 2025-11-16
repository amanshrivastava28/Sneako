import { render, screen } from '@testing-library/react';
import axios from 'axios';
import { test, expect, vi } from 'vitest';
import { MemoryRouter } from 'react-router-dom';

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom');
  return {
    ...actual,
    useParams: () => ({ orderId: '1' }),
    useNavigate: () => vi.fn(),
    useLocation: () => ({ pathname: '/' }),
  };
});
vi.mock('axios');

import OrderTracking from '../Pages/OrderTracking';

test('renders Order Tracking heading', async () => {
  axios.get.mockImplementation((url) => {
    if (url.includes('/order-service/order/')) {
      return Promise.resolve({
        data: {
          orderId: 1,
          orderDate: new Date().toISOString(),
          orderStatus: 'Processing',
          orderItems: [
            { productId: 1, quantity: 1, unitPrice: 1000 }
          ],
          totalPrice: 1000,
        }
      });
    }
    if (url.includes('/product-service/product/')) {
      return Promise.resolve({
        data: {
          productID: 1,
          productName: 'Test Shoe',
          imageUrl: 'test.jpg'
        }
      });
    }
    return Promise.resolve({ data: {} });
  });

  render(
    <MemoryRouter>
      <OrderTracking />
    </MemoryRouter>
  );

  expect(await screen.findByText(/Order Tracking/i)).toBeInTheDocument();
});
