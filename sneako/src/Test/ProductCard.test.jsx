import { render, screen } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { test, expect } from 'vitest';
import ProductCard from '../Pages/productCard';

test('renders product details and new arrival badge', () => {
  const product = {
    productID: 20,              
    productName: 'Sneako Xtreme', 
    price: 2999,
    categoryName: 'Running',    
    imageUrl: 'test.jpg',      
  };

  render(
    <MemoryRouter>
      <ProductCard product={product} />
    </MemoryRouter>
  );

  expect(screen.getByText('Sneako Xtreme')).toBeInTheDocument();
  expect(screen.getByText('Running')).toBeInTheDocument();
  expect(screen.getByText('₹2,999')).toBeInTheDocument();
  expect(screen.getByText('NEW ARRIVAL')).toBeInTheDocument();
  expect(screen.getByRole('button', { name: /view details/i })).toBeInTheDocument();
  expect(screen.getByRole('link')).toHaveAttribute('href', '/product/20');
});

test('does not show new arrival badge for old product', () => {
  const product = {
    productID: 5,               // ✅ use productID
    productName: 'Sneako Classic',
    price: 1999,
    categoryName: 'Casual',
    imageUrl: 'classic.jpg',
  };

  render(
    <MemoryRouter>
      <ProductCard product={product} />
    </MemoryRouter>
  );

  expect(screen.queryByText('NEW ARRIVAL')).not.toBeInTheDocument();
});
