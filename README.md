# SWE2011 – Big Data Analytics  
## Digital Assignment – 1  
### Web Scraping (Python) and Data Exploration (Java)

---

## Student Details
- **Name:** Sanket Chavhan  
- **Register Number:** 22MIS1170  
- **Course:** SWE2011 – Big Data Analytics  

---

## Introduction

Data analytics often starts with collecting data from web sources and then analyzing it to understand patterns and trends. Web scraping allows us to extract structured data from websites, while data exploration helps in cleaning, organizing, and transforming the data.

In this assignment, Python is used to scrape data from a website and Java is used to perform data exploration on the scraped dataset.

---

## Website Used for Scraping

**Website:** https://books.toscrape.com  

BooksToScrape is a public demo website created for practicing web scraping. It contains book information such as title, price, rating, and availability, which makes it suitable for academic analysis.

---

## Python Web Scraping

### Libraries Used
- requests  
- BeautifulSoup  
- pandas  

### Data Scraped
The following details were extracted from the website:
- Book Title  
- Price  
- Rating  
- Availability  

### Python Code Used

```python
import requests
from bs4 import BeautifulSoup
import pandas as pd

url = "https://books.toscrape.com/"
response = requests.get(url)
soup = BeautifulSoup(response.text, "lxml")

books = []
rating_map = {"One":1, "Two":2, "Three":3, "Four":4, "Five":5}

for book in soup.select("article.product_pod"):
    title = book.h3.a["title"]
    price_text = book.select_one("p.price_color").text
    price = float("".join(c for c in price_text if c.isdigit() or c == "."))
    rating = rating_map[book.p["class"][1]]
    availability = book.select_one("p.instock.availability").text.strip()
    books.append([title, price, rating, availability])

df = pd.DataFrame(
    books,
    columns=["Book Title", "Price", "Rating", "Availability"]
)

df.to_csv("books.csv", index=False, encoding="utf-8")
print("Total books scraped:", len(df))

```

Conclusion : This assignment demonstrates a complete data analytics workflow. Python was used to scrape structured data from a website, and Java was used to perform data exploration tasks such as cleaning, sorting, filtering, and normalization. The project provides hands-on experience in web scraping and exploratory data analysis.
