import requests
from bs4 import BeautifulSoup
import pandas as pd

URL = "https://books.toscrape.com/"

response = requests.get(URL)
soup = BeautifulSoup(response.text, "lxml")

books = []

rating_map = {
    "One": 1,
    "Two": 2,
    "Three": 3,
    "Four": 4,
    "Five": 5
}

for book in soup.select("article.product_pod"):
    title = book.h3.a["title"]
    price_text = book.select_one("p.price_color").text
    price = float("".join(c for c in price_text if c.isdigit() or c == "."))
    availability = book.select_one("p.instock.availability").text.strip()
    rating_class = book.p["class"][1]
    rating = rating_map[rating_class]

    books.append([title, price, rating, availability])

df = pd.DataFrame(
    books,
    columns=["Book Title", "Price", "Rating", "Availability"]
)

df.to_csv("books.csv", index=False, encoding="utf-8")

print("Scraping completed successfully")
print(df.head())
print("Total books scraped:", len(df))
