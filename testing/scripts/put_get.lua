cur_request = math.random(1,2)
if cur_request == 1 then
  request = function()
    wrk.headers["Connection"] = "Keep-Alive"
    param_value = math.random(1,1000)
    wrk.body = "value" .. param_value
    path = "/v0/entity?id=key" .. param_value
    return wrk.format("PUT", path)
  end
else
  request = function()
    wrk.headers["Connection"] = "Keep-Alive"
    param_value = math.random(1,1000)
    path = "/v0/entity?id=key" .. param_value
    return wrk.format("GET", path)
  end
end
