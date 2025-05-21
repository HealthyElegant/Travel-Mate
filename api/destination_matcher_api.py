import json
from http.server import BaseHTTPRequestHandler, HTTPServer

DESTINATIONS = [
    {
        "name": "Paris",
        "country": "France",
        "description": "The city of lights with art and culture.",
        "categories": ["romance", "art", "cuisine"],
        "moods": ["romantic", "curious"],
        "hidden_gem": False
    },
    {
        "name": "Machu Picchu",
        "country": "Peru",
        "description": "Ancient Incan city in the mountains.",
        "categories": ["history", "adventure", "nature"],
        "moods": ["adventurous", "curious"],
        "hidden_gem": False
    },
    {
        "name": "Reykjavik",
        "country": "Iceland",
        "description": "Gateway to natural wonders and northern lights.",
        "categories": ["nature", "relaxation"],
        "moods": ["relaxed", "adventurous"],
        "hidden_gem": False
    },
    {
        "name": "Hoi An",
        "country": "Vietnam",
        "description": "Historic lantern-lit town with hidden cafes.",
        "categories": ["culture", "food", "romance"],
        "moods": ["curious", "romantic"],
        "hidden_gem": True
    },
    {
        "name": "Bhutan",
        "country": "Bhutan",
        "description": "Land of monasteries and mountains.",
        "categories": ["spirituality", "nature", "adventure"],
        "moods": ["peaceful", "adventurous"],
        "hidden_gem": True
    }
]

ACTIVITIES = [
    {
        "name": "Local food tour",
        "description": "Taste the best dishes with a guided tour.",
        "categories": ["cuisine", "food"],
        "moods": ["curious", "adventurous"]
    },
    {
        "name": "Museum visit",
        "description": "Explore history and art exhibits.",
        "categories": ["history", "art"],
        "moods": ["curious"]
    },
    {
        "name": "Hiking",
        "description": "Hit the trails and enjoy nature.",
        "categories": ["adventure", "nature"],
        "moods": ["adventurous", "relaxed"]
    },
    {
        "name": "Meditation retreat",
        "description": "Find inner peace in tranquil surroundings.",
        "categories": ["spirituality"],
        "moods": ["peaceful", "relaxed"]
    },
    {
        "name": "Night city tour",
        "description": "Discover nightlife and hidden spots in the city.",
        "categories": ["romance", "culture"],
        "moods": ["romantic", "curious"]
    }
]

def score_item(item, preferences, mood):
    score = 0
    if mood and mood in item.get("moods", []):
        score += 2
    for pref in preferences:
        if pref in item.get("categories", []):
            score += 1
    return score

def recommend_items(data_list, preferences, mood, limit=3):
    results = []
    for item in data_list:
        score = score_item(item, preferences, mood)
        if score > 0:
            entry = item.copy()
            entry["score"] = score
            results.append(entry)
    results.sort(key=lambda x: x["score"], reverse=True)
    return results[:limit]

class RequestHandler(BaseHTTPRequestHandler):
    def _set_headers(self, status=200):
        self.send_response(status)
        self.send_header("Content-Type", "application/json")
        self.end_headers()

    def do_POST(self):
        if self.path != "/recommendations":
            self._set_headers(404)
            self.wfile.write(json.dumps({"error": "not found"}).encode())
            return
        length = int(self.headers.get('content-length', 0))
        try:
            data = json.loads(self.rfile.read(length))
        except json.JSONDecodeError:
            self._set_headers(400)
            self.wfile.write(json.dumps({"error": "invalid json"}).encode())
            return
        preferences = [p.lower() for p in data.get("preferences", [])]
        mood = data.get("mood", "").lower()
        dests = recommend_items(DESTINATIONS, preferences, mood)
        acts = recommend_items(ACTIVITIES, preferences, mood)
        gems = [d for d in dests if d.get("hidden_gem")]
        response = {
            "destinations": dests,
            "activities": acts,
            "hidden_gems": gems
        }
        self._set_headers()
        self.wfile.write(json.dumps(response).encode())


def run(port=8000):
    httpd = HTTPServer(("", port), RequestHandler)
    print(f"Serving on port {port}")
    httpd.serve_forever()

if __name__ == "__main__":
    run()
