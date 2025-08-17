#!/usr/bin/env python3
"""
Initialize/refresh the `sch_jobs` table inside an existing MySQL database (from docker-compose).

What this script does:
- Connects to the compose-created DB (default: appdb) as the app user
- DROP TABLE IF EXISTS sch_jobs
- CREATE TABLE sch_jobs
- Insert NUM_ROWS randomized rows

Requirements:
    pip install mysql-connector-python
"""

import time
import random
import sys
import os
from typing import List, Tuple

try:
    import mysql.connector
except ImportError:
    sys.stderr.write("Missing dependency. Install with: pip install mysql-connector-python\n")
    raise

MYSQL_HOST = "127.0.0.1"
MYSQL_PORT = 3306
MYSQL_USER = os.getenv("DB_USER")
MYSQL_PASSWORD = os.getenv("DB_PASS")
DB_NAME = "appdb"
TABLE_NAME = "sch_jobs"

# Number of rows to insert (configurable hardcoded value)
NUM_ROWS = 1000  # <-- change this as needed

JOB_TYPES = [
    "AddNumbersJob",
    "FibonacciJob",
    "FileWriteReadJob",
    "HashJob",
    "MatrixMultiplyJob",
    "PiMonteCarloJob",
    "RandomPrimeJob",
    "RandomWalkJob",
    "SleepJob",
    "SortJob",
    "WordCountJob",
]

def connect():
    """Create a MySQL connection to the target database."""
    return mysql.connector.connect(
        host=MYSQL_HOST,
        port=MYSQL_PORT,
        user=MYSQL_USER,
        password=MYSQL_PASSWORD,
        database=DB_NAME,
    )

def recreate_table():
    """Drop and recreate the sch_jobs table inside the existing database."""
    ddl_drop = f"DROP TABLE IF EXISTS `{TABLE_NAME}`"
    ddl_create = f"""
    CREATE TABLE `{TABLE_NAME}` (
        `id` BIGINT NOT NULL,
        `site_id` BIGINT NOT NULL,
        `job_type` VARCHAR(64) NOT NULL,
        `status` VARCHAR(32) NOT NULL,
        `created_at` BIGINT NOT NULL,
        `updated_at` BIGINT NOT NULL,
        `scheduled_at` BIGINT NOT NULL,
        PRIMARY KEY (`id`),
        KEY `idx_status_scheduled` (`status`, `scheduled_at`),
        KEY `idx_job_type` (`job_type`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
    """
    conn = connect()
    try:
        cur = conn.cursor()
        cur.execute(ddl_drop)
        cur.execute(ddl_create)
        conn.commit()
        print(f"[OK] Table `{DB_NAME}.{TABLE_NAME}` dropped and recreated.")
    finally:
        conn.close()

def generate_rows(n: int) -> List[Tuple[int, int, str, str, int, int, int]]:
    """
    Generate n rows:
      id: 1..n
      site_id: same as id
      job_type: random choice from JOB_TYPES
      status: "SCHEDULED"
      created_at, updated_at: current epoch seconds
      scheduled_at: random epoch between now and 15 minutes from now (inclusive)
    """
    now = int(time.time())
    horizon = now + 15 * 60
    choose = random.choice
    randint = random.randint
    rows = []
    for i in range(1, n + 1):
        rows.append((
            i,                      # id
            i,                      # site_id
            choose(JOB_TYPES),      # job_type
            "SCHEDULED",            # status
            now,                    # created_at
            now,                    # updated_at
            randint(now, horizon),  # scheduled_at
        ))
    return rows

def insert_rows(rows: List[Tuple[int, int, str, str, int, int, int]]):
    """Bulk insert generated rows into sch_jobs."""
    sql = f"""
        INSERT INTO `{TABLE_NAME}`
            (`id`, `site_id`, `job_type`, `status`, `created_at`, `updated_at`, `scheduled_at`)
        VALUES (%s, %s, %s, %s, %s, %s, %s)
    """
    conn = connect()
    try:
        cur = conn.cursor()
        cur.executemany(sql, rows)
        conn.commit()
        print(f"[OK] Inserted {cur.rowcount} rows into `{DB_NAME}.{TABLE_NAME}`.")
    finally:
        conn.close()

def main():
    recreate_table()
    rows = generate_rows(NUM_ROWS)
    insert_rows(rows)
    print("[DONE] Seeding complete.")

if __name__ == "__main__":
    main()
