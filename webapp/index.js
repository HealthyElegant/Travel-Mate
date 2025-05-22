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

app.post('/api/quiz', (req, res) => {
  const answers = req.body;
  const recommendations = getRecommendations(answers);
  res.json({ recommendations });
});

app.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});
