# Нагрузочное тестирование

Для тестирования был настроен кластер из трёх узлов, развёрнутый в виртуальной машине и контейнерах, содержащихся в виртуальной машине. Для создания виртуальной машины использовался Oracle VirtualBox. На виртуальную машину была установлена ОС Ubuntu LTE 16.04. Была насроено сетевое подключение между гостевой системой (Ubuntu LTE 16.04) и хостовой системой (Windows 10). Ещё два узла кластера запускались в linux контейнерах (LXC, использовались предоставленные образы ubuntu-daily:16.04).

Нагрузка происходила с помощью утилиты wrk. Тестирование происходило тремя скриптами: GET, PUT, PUT_GET, в течение 1 минуты, на 1,2 и 4 тредах и 1,2 и 4 коннектах, каждый. Скрипты расположены в папке scripts.

## До оптимизаций

### Put:

**1 тред, 1 коннект**
```
$ wrk --latency -t1 -c1 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.02ms    1.37ms  52.14ms   96.11%
    Req/Sec    22.69      4.52    30.00     71.86%
  Latency Distribution
     50%   44.02ms
     75%   44.20ms
     90%   44.58ms
     99%   48.10ms
  1362 requests in 1.00m, 155.46KB read
Requests/sec:     22.69
Transfer/sec:      2.59KB
```

**2 треда, 2 коннекта**

```
$ wrk --latency -t2 -c2 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    53.86ms   64.81ms 892.34ms   96.89%
    Req/Sec    22.25      5.06    30.00     70.65%
  Latency Distribution
     50%   44.01ms
     75%   44.29ms
     90%   44.86ms
     99%  385.90ms
  2610 requests in 1.00m, 298.06KB read
Requests/sec:     43.44
Transfer/sec:      4.96KB
```

**4 треда, 4 коннекта**

```
$ wrk --latency -t4 -c4 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.99ms    8.12ms 207.01ms   98.70%
    Req/Sec    22.37      4.64    30.00     72.50%
  Latency Distribution
     50%   44.02ms
     75%   44.31ms
     90%   45.56ms
     99%   71.02ms
  5368 requests in 1.00m, 613.02KB read
Requests/sec:     89.33
Transfer/sec:     10.20KB
```

### Get:

**1 тред, 1 коннект**

```
$ wrk --latency -t1 -c1 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    47.53ms   20.48ms 293.28ms   96.87%
    Req/Sec    22.08      4.96    30.00     72.57%
  Latency Distribution
     50%   44.05ms
     75%   44.43ms
     90%   47.45ms
     99%  160.82ms
  1309 requests in 1.00m, 107.30KB read
Requests/sec:     21.78
Transfer/sec:      1.79K
```

**2 треда, 2 коннекта**

```
$ wrk --latency -t2 -c2 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    47.00ms   24.02ms 455.79ms   98.04%
    Req/Sec    22.28      4.77    30.00     72.42%
  Latency Distribution
     50%   44.03ms
     75%   44.31ms
     90%   45.15ms
     99%  145.76ms
  2655 requests in 1.00m, 217.62KB read
Requests/sec:     44.21
Transfer/sec:      3.62KB
```

**4 треда, 4 коннекта**

```
$ wrk --latency -t4 -c4 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.46ms    7.66ms 251.60ms   99.54%
    Req/Sec    22.62      4.54    30.00     72.15%
  Latency Distribution
     50%   44.02ms
     75%   44.24ms
     90%   44.64ms
     99%   48.36ms
  5426 requests in 1.00m, 444.68KB read
Requests/sec:     90.35
Transfer/sec:      7.40KB
```

### Put_Get:

**1 тред, 1 коннект**

```
$ wrk --latency -t1 -c1 -d1m -s put_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    46.36ms   25.17ms 508.36ms   99.19%
    Req/Sec    22.43      4.48    30.00     74.07%
  Latency Distribution
     50%   44.06ms
     75%   44.30ms
     90%   46.94ms
     99%   56.74ms
  1340 requests in 1.00m, 108.53KB read
Requests/sec:     22.31
Transfer/sec:      1.81KB
```

**2 треда, 2 коннекта**

```
$ wrk --latency -t2 -c2 -d1m -s put_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
^[[A^[[  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    45.37ms   15.85ms 384.21ms   99.08%
    Req/Sec    22.49      4.56    30.00     72.86%
  Latency Distribution
     50%   44.04ms
     75%   44.27ms
     90%   44.89ms
     99%   52.67ms
  2693 requests in 1.00m, 218.12KB read
Requests/sec:     44.82
Transfer/sec:      3.63KB
```

**4 треда, 4 коннекта**

```
$ wrk --latency -t4 -c4 -d1m -s put_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    48.85ms   33.42ms 544.98ms   97.46%
    Req/Sec    22.07      4.83    30.00     72.79%
  Latency Distribution
     50%   44.07ms
     75%   44.49ms
     90%   47.87ms
     99%  199.92ms
  5234 requests in 1.00m, 423.93KB read
Requests/sec:     87.15
Transfer/sec:      7.06KB
```

## Профилирование

Для профилирования использовался async-profiler, который подключался к java машине, запущенной на виртуальной машине. На время профилирование (30 секунд) на кластер подавалась нагрузка из смешанных PUT и GET запросов с помощью ранее реализованного lua скрипта и утириты wrk. Результаты профилирования были сохранены в файл и преобразованы в график с помощью скрипта flamegrahp.pl (https://github.com/BrendanGregg/FlameGraph).

Ниже приведён полученный график с результатами профилирования:
<img src="testing/flamegraph.svg">

Про профилировав и увидев, что сервер тратит крайне мало времени на создание потоков и ознакомившись с документацией по серверу, было принято решение использовать класс Executor для распределения нагрузки по потокам.

## После оптимизаций

После внесения измениний, код был протестирован вышеупомянутыми скриптами.

### Put:

**1 тред, 1 коннект**
```
$ wrk --latency -t1 -c1 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.02ms    1.37ms  52.14ms   96.11%
    Req/Sec    22.69      4.52    30.00     71.86%
  Latency Distribution
     50%   44.02ms
     75%   44.20ms
     90%   44.58ms
     99%   48.10ms
  1362 requests in 1.00m, 155.46KB read
Requests/sec:     22.69
Transfer/sec:      2.59KB
```

**2 треда, 2 коннекта**

```
$ wrk --latency -t2 -c2 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    53.86ms   64.81ms 892.34ms   96.89%
    Req/Sec    22.25      5.06    30.00     70.65%
  Latency Distribution
     50%   44.01ms
     75%   44.29ms
     90%   44.86ms
     99%  385.90ms
  2610 requests in 1.00m, 298.06KB read
Requests/sec:     43.44
Transfer/sec:      4.96KB
```

**4 треда, 4 коннекта**

```
$ wrk --latency -t4 -c4 -d1m -s put.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.99ms    8.12ms 207.01ms   98.70%
    Req/Sec    22.37      4.64    30.00     72.50%
  Latency Distribution
     50%   44.02ms
     75%   44.31ms
     90%   45.56ms
     99%   71.02ms
  5368 requests in 1.00m, 613.02KB read
Requests/sec:     89.33
Transfer/sec:     10.20KB
```

### Get:

**1 тред, 1 коннект**

```
$ wrk --latency -t1 -c1 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    47.53ms   20.48ms 293.28ms   96.87%
    Req/Sec    22.08      4.96    30.00     72.57%
  Latency Distribution
     50%   44.05ms
     75%   44.43ms
     90%   47.45ms
     99%  160.82ms
  1309 requests in 1.00m, 107.30KB read
Requests/sec:     21.78
Transfer/sec:      1.79K
```

**2 треда, 2 коннекта**

```
$ wrk --latency -t2 -c2 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    47.00ms   24.02ms 455.79ms   98.04%
    Req/Sec    22.28      4.77    30.00     72.42%
  Latency Distribution
     50%   44.03ms
     75%   44.31ms
     90%   45.15ms
     99%  145.76ms
  2655 requests in 1.00m, 217.62KB read
Requests/sec:     44.21
Transfer/sec:      3.62KB
```

**4 треда, 4 коннекта**

```
$ wrk --latency -t4 -c4 -d1m -s get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    44.46ms    7.66ms 251.60ms   99.54%
    Req/Sec    22.62      4.54    30.00     72.15%
  Latency Distribution
     50%   44.02ms
     75%   44.24ms
     90%   44.64ms
     99%   48.36ms
  5426 requests in 1.00m, 444.68KB read
Requests/sec:     90.35
Transfer/sec:      7.40KB
```

### Put_Get:

**1 тред, 1 коннект**

```
$ wrk --latency -t1 -c1 -d1m -s put_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  1 threads and 1 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    46.36ms   25.17ms 508.36ms   99.19%
    Req/Sec    22.43      4.48    30.00     74.07%
  Latency Distribution
     50%   44.06ms
     75%   44.30ms
     90%   46.94ms
     99%   56.74ms
  1340 requests in 1.00m, 108.53KB read
Requests/sec:     22.31
Transfer/sec:      1.81KB
```

**2 треда, 2 коннекта**

```
$ wrk --latency -t2 -c2 -d1m -s put_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  2 threads and 2 connections
^[[A^[[  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    45.37ms   15.85ms 384.21ms   99.08%
    Req/Sec    22.49      4.56    30.00     72.86%
  Latency Distribution
     50%   44.04ms
     75%   44.27ms
     90%   44.89ms
     99%   52.67ms
  2693 requests in 1.00m, 218.12KB read
Requests/sec:     44.82
Transfer/sec:      3.63KB
```

**4 треда, 4 коннекта**

```
$ wrk --latency -t4 -c4 -d1m -s put_get.lua http://localhost:8080
Running 1m test @ http://localhost:8080
  4 threads and 4 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency    48.85ms   33.42ms 544.98ms   97.46%
    Req/Sec    22.07      4.83    30.00     72.79%
  Latency Distribution
     50%   44.07ms
     75%   44.49ms
     90%   47.87ms
     99%  199.92ms
  5234 requests in 1.00m, 423.93KB read
Requests/sec:     87.15
Transfer/sec:      7.06KB
```
## Профилирование

Ниже приведён полученный график с результатами профилирования:
<img src="testing/flamegraphNew.svg">

# Вывод 
Был создан сервер с использование библиотек java. Был проведено тестирование сервера, по результатам тестирования были внесены исправленя в код. Но так как данный сервер, судя по API, сам по себе не поддерживает ассинхронность в понимании NIO, добиться серьезных улучшений не удается.
