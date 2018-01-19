request = function()
  wrk.headers["Connection"] = "Keep-Alive"
  param_value = math.random(1,10000)
  wrk.body = "value" .. param_value
  path = "/v0/entity?id=key" .. param_value
  return wrk.format("PUT", path)
end
