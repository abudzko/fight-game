import { render, screen } from '@testing-library/react';
import App from './App';

test('buttons', () => {
  render(<App />);
  const createFightButton = screen.getByText(/Create Fight/i);
  expect(createFightButton).toBeInTheDocument();
  const joinFightButton = screen.getByText(/Join Fight/i);
  expect(joinFightButton).toBeInTheDocument();
  const joinRandomFightButton = screen.getByText(/Join Random Fight/i);
  expect(joinRandomFightButton).toBeInTheDocument();
});
