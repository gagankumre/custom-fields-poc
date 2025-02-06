import psycopg2
import json
import random
import string
import time

# Database connection configuration
DB_CONFIG = {
    "host": "localhost",
    "database": "testdb",
    "user": "yourusername",
    "password": "yourpassword",
    "port": 5432
}

# Number of test inserts
NUM_TEST_INSERTS = 100  # Change this value as needed to adjust the number of writes

# Generate random JSON data of approximately 4MB
def generate_4mb_json():
    json_size_mb = 4
    key_length = 10
    value_length = 100
    num_entries = (json_size_mb * 1024 * 1024) // (key_length + value_length)

    random_json = {
        ''.join(random.choices(string.ascii_letters, k=key_length)):
        ''.join(random.choices(string.ascii_letters + string.digits, k=value_length))
        for _ in range(num_entries)
    }
    return json.dumps(random_json)


# Create a test table
def create_table(cursor):
    cursor.execute("""
        DROP TABLE IF EXISTS test_jsonb_performance;
        CREATE TABLE test_jsonb_performance (
            id SERIAL PRIMARY KEY,
            data JSONB
        );
    """)


# Perform the write test
def run_test(conn):
    cursor = conn.cursor()

    print("Generating 4MB JSON data for testing...")
    large_json = generate_4mb_json()

    print("Starting write test...")
    start_time = time.time()

    for i in range(NUM_TEST_INSERTS):
        cursor.execute("""
            INSERT INTO test_jsonb_performance (data) VALUES (%s);
        """, (large_json,))
        conn.commit()

        # Print progress
        if (i + 1) % 10 == 0:
            print(f"Inserted {i + 1}/{NUM_TEST_INSERTS} rows...")

    end_time = time.time()
    total_time = end_time - start_time
    avg_writes_per_second = NUM_TEST_INSERTS / total_time

    print("\nTest Completed!")
    print(f"Total time: {total_time:.2f} seconds")
    print(f"Average writes per second: {avg_writes_per_second:.2f}")

    cursor.close()


def main():
    try:
        print("Connecting to the database...")
        conn = psycopg2.connect(**DB_CONFIG)

        cursor = conn.cursor()
        print("Creating test table...")
        create_table(cursor)
        conn.commit()

        run_test(conn)

    except Exception as e:
        print(f"An error occurred: {e}")
    finally:
        if conn:
            conn.close()
        print("Connection closed.")


if __name__ == "__main__":
    main()