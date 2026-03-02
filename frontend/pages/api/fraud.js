export default function handler(req, res) {
  // simple risk scoring based on data in request body
  const { scenario, amount, location, deviceTrusted, simSwap, userId, deviceId } = req.body;

  // generate a mock score
  let score = Math.floor(Math.random() * 100);

  // bias based on scenario
  if (scenario === 'login') {
    if (simSwap) score += 20;
    if (location && location !== 'Home') score += 10;
  }
  if (scenario === 'transfer') {
    if (amount && amount > 1000) score += 30;
    if (!deviceTrusted) score += 20;
    if (location && location !== 'Home') score += 20;
  }
  if (scenario === 'password') {
    if (simSwap) score += 40;
  }
  if (scenario === 'pay') {
    if (amount && amount > 500) score += 25;
  }

  if (score > 100) score = 100;

  let action = 'ok';
  if (score >= 80) action = 'block';
  else if (score >= 50) action = 'otp';

  return res.status(200).json({ action, score });
}