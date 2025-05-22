const express = require('express');
const bodyParser = require('body-parser');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;

app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(express.static(path.join(__dirname, 'public')));

// Simple recommendation logic based on quiz results
function getRecommendations(answers) {
  const suggestions = [];
  if (answers.travelStyle === 'relaxation') {
    suggestions.push('Visit a quiet beach town.');
  }
  if (answers.travelStyle === 'adventure') {
    suggestions.push('Try hiking in the mountains.');
  }
  if (parseInt(answers.budget, 10) < 1000) {
    suggestions.push('Look for budget-friendly hostels.');
  }
  if (answers.interests.includes('food')) {
    suggestions.push('Explore local food markets.');
  }
  return suggestions;
}

// Sample activities for itinerary generation
const ITINERARY_ACTIVITIES = [
  'City walking tour',
  'Museum visit',
  'Local food tasting',
  'Hike in nature',
  'Relax at the beach',
  'Explore nightlife',
  'Visit historical site'
];

// In-memory storage for itineraries keyed by userId
const userItineraries = {};

function generateItinerary(days) {
  const itinerary = [];
  for (let i = 0; i < days; i++) {
    const dayPlan = {
      day: i + 1,
      activities: []
    };
    const shuffled = [...ITINERARY_ACTIVITIES].sort(() => 0.5 - Math.random());
    dayPlan.activities = shuffled.slice(0, 3);
    itinerary.push(dayPlan);
  }
  return itinerary;
}

app.post('/api/quiz', (req, res) => {
  const answers = req.body;
  const recommendations = getRecommendations(answers);
  res.json({ recommendations });
});

// Generate itinerary for a user and store it
app.post('/api/itinerary/generate', (req, res) => {
  const { userId, days } = req.body;
  const numDays = parseInt(days, 10) || 3;
  const itinerary = generateItinerary(numDays);
  if (userId) {
    userItineraries[userId] = itinerary;
  }
  res.json({ itinerary });
});

// Save reordered itinerary for a user
app.post('/api/itinerary/save', (req, res) => {
  const { userId, itinerary } = req.body;
  if (!userId || !Array.isArray(itinerary)) {
    return res.status(400).json({ error: 'invalid request' });
  }
  userItineraries[userId] = itinerary;
  res.json({ success: true });
});

// Retrieve itinerary for a user
app.get('/api/itinerary/:userId', (req, res) => {
  const { userId } = req.params;
  const itinerary = userItineraries[userId];
  if (!itinerary) {
    return res.status(404).json({ error: 'not found' });
  }
  res.json({ itinerary });
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
